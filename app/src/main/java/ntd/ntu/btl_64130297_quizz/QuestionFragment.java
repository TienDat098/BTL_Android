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
        questionList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Questions");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionList.clear(); // Xóa danh sách cũ để tránh trùng lặp
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    if (question != null) {
                        // Lấy tên ảnh từ image_MinhHoa (là tên string như "anhcau1")
                        int imageResId = getResources().getIdentifier(
                                question.getImage_MinhHoa(), "mipmap", requireContext().getPackageName());
                        if (imageResId == 0) {
                            Log.w("ImageNotFound", "Hình ảnh không tìm thấy: " + question.getImage_MinhHoa());
                        }
                        // Nếu không tìm thấy thì dùng ảnh mặc định
                        question.setImage_MinhHoaId(imageResId != 0 ? imageResId : R.mipmap.ic_launcher);
                        questionList.add(question);
                        Log.d("FirebaseQuestion", "Loaded: " + question.getQuestion());
                    }
                }
                progressBar.setVisibility(View.GONE);
                // Kiểm tra nếu danh sách rỗng
                if (questionList.isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Lỗi")
                            .setMessage("Không tải được câu hỏi từ Firebase. Vui lòng kiểm tra kết nối hoặc dữ liệu!")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Quay lại HomeFragment hoặc thoát
                                getParentFragmentManager().popBackStack();
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    showQuestion();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu: " + databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
                new AlertDialog.Builder(getContext())
                        .setTitle("Lỗi")
                        .setMessage("Không thể tải câu hỏi từ Firebase: " + databaseError.getMessage())
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Quay lại HomeFragment hoặc thoát
                            getParentFragmentManager().popBackStack();
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }
    private void showQuestion() {
        if (QuestionIndex < questionList.size()) {
            Question q = questionList.get(QuestionIndex);
            tvQuestion.setText(q.getQuestion());
            rbA.setText("A. " + q.getOptionA());
            rbB.setText("B. " + q.getOptionB());
            rbC.setText("C. " + q.getOptionC());
            rbD.setText("D. " + q.getOptionD());
            rgAnswers.clearCheck();
            imgQuestion.setImageResource(q.getImage_MinhHoaId());
            // Đặt lại màu nền về mặc định cho các RadioButton
            rbA.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rbB.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rbC.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rbD.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            startTimer();
        }
    }
    private void submitAnswer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Kiểm tra xem người dùng đã chọn đáp án chưa
        if (rgAnswers.getCheckedRadioButtonId() == -1) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Vui lòng chọn một đáp án trước khi trả lời!")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
        String selectedAnswer = "";
        RadioButton selectedRadioButton = null;
        if (rbA.isChecked()) {
            selectedAnswer = "A";
            selectedRadioButton = rbA;
        } else if (rbB.isChecked()) {
            selectedAnswer = "B";
            selectedRadioButton = rbB;
        } else if (rbC.isChecked()) {
            selectedAnswer = "C";
            selectedRadioButton = rbC;
        } else if (rbD.isChecked()) {
            selectedAnswer = "D";
            selectedRadioButton = rbD;
        }
        // Kiểm tra đáp án đúng hay sai
        boolean isCorrect = selectedAnswer.equals(questionList.get(QuestionIndex).getAnswer());
        if (isCorrect) {
            score++;
            selectedRadioButton.setBackgroundColor(getResources().getColor(R.color.text_green)); // Màu xanh
        } else {
            selectedRadioButton.setBackgroundColor(getResources().getColor(R.color.text_red)); // Màu đỏ
        }
        // Vô hiệu hóa RadioGroup và nút Submit để người dùng không thay đổi đáp án
        rgAnswers.setEnabled(false);
        for (int i = 0; i < rgAnswers.getChildCount(); i++) {
            rgAnswers.getChildAt(i).setEnabled(false);
        }
        btnSubmit.setEnabled(false);
        // Chuyển sang câu hỏi tiếp theo hoặc hiển thị kết quả sau 1 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            QuestionIndex++;
            if (QuestionIndex < questionList.size()) {
                showQuestion();
                // Kích hoạt lại RadioGroup và nút Submit cho câu hỏi tiếp theo
                rgAnswers.setEnabled(true);
                for (int i = 0; i < rgAnswers.getChildCount(); i++) {
                    rgAnswers.getChildAt(i).setEnabled(true);
                }
                btnSubmit.setEnabled(true);
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Kết thúc")
                        .setMessage("Bạn đúng " + score + "/" + questionList.size() + " câu.\nChúc mừng!")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Có thể reset lại Quiz ở đây nếu muốn
                        })
                        .show();
            }
        }, 1000); // Đợi 1 giây trước khi chuyển câu hỏi
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                //xử lý hết giờ
                submitAnswer();
            }
        };
        countDownTimer.start();
    }
}