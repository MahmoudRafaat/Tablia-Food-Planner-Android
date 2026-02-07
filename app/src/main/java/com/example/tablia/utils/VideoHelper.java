package com.example.tablia.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoHelper {

    public static String extractVideoId(String url) {
        if (url == null || url.trim().isEmpty()) return null;

        String pattern = "(?:https?:\\/\\/)?(?:www\\.|m\\.|music\\.)?youtu(?:be\\.com\\/(?:watch\\?v=|embed\\/|v\\/)|\\.be\\/)([\\w-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url.trim());

        if (matcher.find()) return matcher.group(1);
        return null;
    }

    public static void setupVideo(
            @NonNull Context context,
            String youtubeUrl,
            @NonNull ImageView thumbnailView,
            @NonNull YouTubePlayerView playerView,
            @NonNull View container,
            TextView noVideoTextView
    ) {
        final String videoId = extractVideoId(youtubeUrl);

        if (videoId != null && !videoId.isEmpty()) {
            String thumbUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
            Glide.with(context)
                    .load(thumbUrl)
                    .centerCrop()
                    .into(thumbnailView);

            playerView.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
            if (noVideoTextView != null) noVideoTextView.setVisibility(View.GONE);

            container.setOnClickListener(v -> {
                container.setVisibility(View.GONE);
                playerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                    youTubePlayer.loadVideo(videoId, 0);
                });
            });
        } else {
            container.setVisibility(View.GONE);
            playerView.setVisibility(View.GONE);
            if (noVideoTextView != null) noVideoTextView.setVisibility(View.VISIBLE);
        }
    }
}
