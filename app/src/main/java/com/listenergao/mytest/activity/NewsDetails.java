package com.listenergao.mytest.activity;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.listenergao.mytest.R;
import com.listenergao.mytest.data.NewsAdapter;
import com.listenergao.mytest.requestBean.NewsDetailsBean;
import com.listenergao.mytest.utils.OkHttpManager;
import com.listenergao.mytest.utils.UiUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 新闻详情页面
 */
public class NewsDetails extends BaseActivity {

    private static final String TAG ="NewsDetails" ;
    @BindView(R.id.news_webview)
    WebView newsWebview;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        int newsId = getIntent().getIntExtra(NewsAdapter.NEWS_ID, 0);

        requestData(String.valueOf(newsId));
    }

    private void requestData(String newsId) {
        OkHttpManager.getAsyn("http://news-at.zhihu.com/api/4/news/" + newsId, new OkHttpManager.ResultCallback<NewsDetailsBean>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(UiUtils.getContext(),"网络连接失败,请检查网络连接!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(NewsDetailsBean response) {
                Logger.d(TAG,response.getCss().get(0));
                String webViewUrl = response.getCss().get(0);
                initWebView(webViewUrl);

            }
        });
    }

    private void initWebView(String url) {
        newsWebview.getSettings().setJavaScriptEnabled(true);   //设置支持js脚本
        newsWebview.getSettings().setBuiltInZoomControls(true); //设置支持缩放
        newsWebview.loadUrl(url);
        newsWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;    //true 表示此事件被处理,不需要在广播
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(NewsDetails.this, "Oh no! " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
