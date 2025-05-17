package ntd.ntu.btl_64130297_quizz;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuestionFragment extends Fragment {
    private TextView tvHello, tvTimer, tvQuestion;
    private RadioGroup rgAnswers;
    private RadioButton rbA, rbB, rbC, rbD;
    private ProgressBar progressBar;
    private Button btnSubmit;
    private ImageView imgQuestion;
    private List<Question> questionList;
    private int QuestionIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;
    public QuestionFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        // tim dk
        tvHello = view.findViewById(R.id.tv_hello);
        tvTimer = view.findViewById(R.id.tv_timer);
        tvQuestion = view.findViewById(R.id.tv_question);
        rgAnswers = view.findViewById(R.id.rg_answers);
        imgQuestion = view.findViewById(R.id.img_question);
        rbA = view.findViewById(R.id.rb_a);
        rbB = view.findViewById(R.id.rb_b);
        rbC = view.findViewById(R.id.rb_c);
        rbD = view.findViewById(R.id.rb_d);
        btnSubmit = view.findViewById(R.id.btn_submit);
        progressBar = view.findViewById(R.id.progress_bar);

        String userName = "User";
        if (getArguments() != null) {
            userName = getArguments().getString("user_name", "User");
        }
        tvHello.setText("Xin chào " + userName);

        questionList = new ArrayList<>();
        loadQuestions(); // Tải dữ liệu từ Firebase
        btnSubmit.setOnClickListener(v -> submitAnswer());
        return view;
    }
    private void loadQuestions() {
        // Khởi tạo danh sách câu hỏi mới
        questionList = new ArrayList<>();
        // Tham chiếu đến node "Questions" trong Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Questions");
        // Đọc dữ liệu 1 lần từ Firebase (không lắng nghe thay đổi liên tục)
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Xóa danh sách cũ để tránh trùng lặp
                questionList.clear();
                // Duyệt qua từng câu hỏi trong dữ liệu Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu từng câu hỏi và chuyển thành đối tượng Question
                    Question question = snapshot.getValue(Question.class);
                    // Lấy ID ảnh minh họa từ tên ảnh lưu trong Firebase (dạng String như "anhcau1")
                    int imageResId = getResources().getIdentifier(question.getImage_MinhHoa(),    // Tên ảnh
                            "mipmap",                      // Tìm trong thư mục mipmap
                            requireContext().getPackageName() // Lấy package hiện tại của app
                    );
                    // Gán ID ảnh vào thuộc tính image_MinhHoaId của đối tượng Question
                    // Nếu không tìm thấy ảnh, sử dụng ảnh mặc định ic_launcher
                    question.setImage_MinhHoaId(imageResId != 0 ? imageResId : R.mipmap.ic_launcher);
                    // Thêm câu hỏi vào danh sách
                    questionList.add(question);
                }
                // Ẩn ProgressBar sau khi load xong dữ liệu
                progressBar.setVisibility(View.GONE);
                // Hiển thị câu hỏi đầu tiên lên giao diện
                showQuestion();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Ẩn ProgressBar khi có lỗi kết nối Firebase
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void showQuestion() {
        // Kiểm tra còn câu hỏi nào chưa hiển thị
        if (QuestionIndex < questionList.size()) {
            // Lấy câu hỏi hiện tại dựa vào chỉ số
            Question q = questionList.get(QuestionIndex);
            // Hiển thị nội dung câu hỏi
            tvQuestion.setText(q.getQuestion());
            // Hiển thị các lựa chọn đáp án
            rbA.setText("A. " + q.getOptionA());
            rbB.setText("B. " + q.getOptionB());
            rbC.setText("C. " + q.getOptionC());
            rbD.setText("D. " + q.getOptionD());
            // Xóa lựa chọn cũ nếu có
            rgAnswers.clearCheck();
            // Hiển thị hình minh họa cho câu hỏi
            imgQuestion.setImageResource(q.getImage_MinhHoaId());
            // Đặt lại màu nền về mặc định cho tất cả các RadioButton (tránh màu của câu trước)
            rbA.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rbB.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rbC.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rbD.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            // Bắt đầu đếm ngược thời gian cho câu hỏi
            startTimer();
        }
    }
    // Hàm xử lý khi người dùng nhấn nút "Trả lời"
    private void submitAnswer() {
        // Hủy đếm ngược nếu người dùng tự nộp bài
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Kiểm tra người dùng đã chọn đáp án hay chưa
        if (rgAnswers.getCheckedRadioButtonId() == -1) {
            // Nếu chưa chọn, thông báo yêu cầu chọn
            new AlertDialog.Builder(getContext())
                    .setTitle("Kết thúc")
                    .setMessage("Bạn đúng " + score + "/" + questionList.size() + " câu.\nChúc mừng!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Reset lại chỉ số và điểm
                        QuestionIndex = 0;
                        score = 0;

                        // Quay về màn hình chính
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_main, new HomeFragment())
                                .commit();
                    })
                    .setCancelable(false)
                    .show();

        }
        String selectedAnswer = "";
        RadioButton selectedRadioButton = null;
        // Xác định đáp án người dùng đã chọn
        if (rbA.isChecked()) { selectedAnswer = "A"; selectedRadioButton = rbA; }
        else if (rbB.isChecked()) { selectedAnswer = "B"; selectedRadioButton = rbB; }
        else if (rbC.isChecked()) { selectedAnswer = "C"; selectedRadioButton = rbC; }
        else if (rbD.isChecked()) { selectedAnswer = "D"; selectedRadioButton = rbD; }
        // Kiểm tra đáp án có đúng hay không
        boolean isCorrect = selectedAnswer.equals(questionList.get(QuestionIndex).getAnswer());
        if (isCorrect) {
            score++; // Tăng điểm nếu đúng
            selectedRadioButton.setBackgroundColor(getResources().getColor(R.color.text_green)); // Hiển thị màu xanh
        } else {
            selectedRadioButton.setBackgroundColor(getResources().getColor(R.color.text_red)); // Hiển thị màu đỏ
        }
        // Vô hiệu hóa RadioGroup và nút Submit để người dùng không thay đổi đáp án
        rgAnswers.setEnabled(false);
        for (int i = 0; i < rgAnswers.getChildCount(); i++) {
            rgAnswers.getChildAt(i).setEnabled(false);
        }
        btnSubmit.setEnabled(false);
        // Sau 1 giây, chuyển sang câu hỏi tiếp theo hoặc kết thúc bài quiz
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            QuestionIndex++;
            if (QuestionIndex < questionList.size()) {
                showQuestion(); // Hiển thị câu hỏi tiếp theo
                // Kích hoạt lại các lựa chọn và nút Submit
                rgAnswers.setEnabled(true);
                for (int i = 0; i < rgAnswers.getChildCount(); i++) {
                    rgAnswers.getChildAt(i).setEnabled(true);
                }
                btnSubmit.setEnabled(true);
            } else {
                // Thông báo kết quả cuối cùng
                new AlertDialog.Builder(getContext())
                        .setTitle("Kết thúc")
                        .setMessage("Bạn đúng " + score + "/" + questionList.size() + " câu.\nChúc mừng!")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Có thể thêm chức năng reset quiz ở đây nếu muốn
                        })
                        .show();
            }
        }, 1000); // Đợi 1 giây
    }

    private void startTimer() {
        // Hủy bộ đếm cũ nếu có
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Tạo mới CountDownTimer 30 giây (đếm lùi từng giây)
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Cập nhật số giây còn lại lên TextView
                tvTimer.setText(millisUntilFinished / 1000 + "s");
            }
            @Override
            public void onFinish() {
                // Khi hết giờ, tự động gọi submitAnswer()
                submitAnswer();
            }
        };
        // Bắt đầu đếm ngược
        countDownTimer.start();
    }

}