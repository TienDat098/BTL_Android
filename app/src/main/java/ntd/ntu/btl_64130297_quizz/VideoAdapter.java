package ntd.ntu.btl_64130297_quizz;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter  extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    private Context context;
    private List<VideoItem> videoList;

    public VideoAdapter(Context context, List<VideoItem> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem video = videoList.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.tvMoTa.setText(video.getUrl());
        holder.imgVideo.setImageResource(video.getImage_video());

        holder.itemView.setOnClickListener(v -> {
            Uri uri = Uri.parse(video.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return videoList.size();
    }
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMoTa;
        ImageView imgVideo;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMoTa = itemView.findViewById(R.id.tv_MoTa);
            imgVideo = itemView.findViewById(R.id.imgVideo);
        }
    }
}
