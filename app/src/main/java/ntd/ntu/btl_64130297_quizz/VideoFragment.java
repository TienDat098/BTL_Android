package ntd.ntu.btl_64130297_quizz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class VideoFragment extends Fragment {

    RecyclerView recyclerView;
    List<VideoItem> videoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //danh sach video

        videoList = new ArrayList<>();
        videoList.add(new VideoItem("Chiến dịch Điện Biên Phủ 1954", "https://youtu.be/jy7Z3oYOp7w?si=B7erHG0hwEqhGIQ5",R.mipmap.dienbienphu));
        videoList.add(new VideoItem("Bác Hồ đọc tuyên ngôn độc lập", "https://youtu.be/34GKvR8nZus?si=iBOGR0R_jy5KIz3T",R.mipmap.doclap));
        videoList.add(new VideoItem("Tổng tiến công Mậu Thân 1968", "https://youtu.be/mEf62v9rzus?si=GqJILyUG3Ee_LQBv",R.mipmap.mauthan));
        videoList.add(new VideoItem("Phong trào Đồng Khởi 1960", "https://youtu.be/qlUTb3Ain5A?si=-fjfE94zvnt2NbkD",R.mipmap.dongkhoi));
        videoList.add(new VideoItem("Ký kết Hiệp định Paris 1973", "https://youtu.be/rpwNyHuoPwU?si=91a_ccrRPK6xuOR5",R.mipmap.hiepdinh));
        videoList.add(new VideoItem("Giải phóng miền Nam 30/4/1975", "https://youtu.be/bDkJJjmmDxE?si=4PGp_a7q4Tv0StIN",R.mipmap.giaiphong));
        VideoAdapter adapter = new VideoAdapter(getContext(), videoList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}