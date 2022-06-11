package com.natrocare.naturocare.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.natrocare.naturocare.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel mViewModel;

    WebView webView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notifications_fragment, container, false);
        webView = root.findViewById(R.id.WebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.com");
        return root;
    }
}