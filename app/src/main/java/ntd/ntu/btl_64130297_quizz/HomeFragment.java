package ntd.ntu.btl_64130297_quizz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class HomeFragment extends Fragment {
    EditText edtName;
    Button btnStart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        edtName = view.findViewById(R.id.edt_name);
        btnStart = view.findViewById(R.id.btn_start);

        btnStart.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
            } else {
                // Chuyển sang QuestionFragment và gửi tên
                Bundle bundle = new Bundle();
                bundle.putString("user_name", name);

                QuestionFragment questionFragment = new QuestionFragment();
                questionFragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_main, questionFragment); // id Layout cha chứa Fragment
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}