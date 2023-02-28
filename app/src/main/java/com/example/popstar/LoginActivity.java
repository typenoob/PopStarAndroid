package com.example.popstar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.popstar.endpoint.UserResponse;
import com.example.popstar.endpoint.UserService;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private View loginView, registerView;
    private UserService userService;
    private List<View> viewList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView username, password, username_r, password_r, email_r;
    private Button login, register,offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        if(getIntent().getBooleanExtra("loginExpired",false)){
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "登录失效", BaseTransientBottomBar.LENGTH_SHORT);
            snackbar.show();
        }
        App context=(App) getApplication();
        context.setIs_login(false);
        context.setToken("");
        login.setOnClickListener(view -> {
            if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                Snackbar snackbar = Snackbar.make(view, "用户名或密码为空", BaseTransientBottomBar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            Call<UserResponse> call = userService.login(new UserService.UserRequest(username.getText().toString(), password.getText().toString()));
            //讲解一
            call.enqueue(new Callback<UserResponse>() {
                //请求成功时回调
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    //请求处理,输出结果
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            App context = (App) getApplication();
                            context.setIs_login(true);
                            context.setToken(response.body().getData().getToken());
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } else if (response.code() == 401) {
                        Snackbar snackbar = Snackbar.make(view, "用户名或密码错误", BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }

                //请求失败时候的回调
                @Override
                public void onFailure(Call<UserResponse> call, Throwable throwable) {
                    System.out.println("连接失败");
                    Snackbar snackbar = Snackbar.make(view, "服务器未响应", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
        });
        offline.setOnClickListener(view->{
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            intent.putExtra("offline",true);
            startActivity(intent);
        });
        register.setOnClickListener(view -> {
            if (username_r.getText().toString().equals("") || password_r.getText().toString().equals("")
                    || email_r.getText().toString().equals("")) {
                Snackbar snackbar = Snackbar.make(view, "用户名，密码或邮箱为空", BaseTransientBottomBar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            Call<UserResponse> call = userService.register(new UserService.UserRequest(username_r.getText().toString(), password_r.getText().toString(), username_r.getText().toString()));
            call.enqueue(new Callback<UserResponse>() {
                //请求成功时回调
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    //请求处理,输出结果
                    if (response.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(view, response.body().getMsg(), BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }

                //请求失败时候的回调
                @Override
                public void onFailure(Call<UserResponse> call, Throwable throwable) {
                    System.out.println("连接失败");
                }
            });
        });
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "登录";
                    case 1:
                        return "注册";
                }
                return null;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//讲解四：Tablayout和viewPager

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void init() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        LayoutInflater inflater = getLayoutInflater();
        loginView = inflater.inflate(R.layout.tab_login, null);
        registerView = inflater.inflate(R.layout.tab_register, null);
        viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        viewList.add(loginView);
        viewList.add(registerView);
        username = loginView.findViewById(R.id.login_username);
        password = loginView.findViewById(R.id.login_password);
        login = loginView.findViewById(R.id.login_btn);
        offline= loginView.findViewById(R.id.offline);
        username_r = registerView.findViewById(R.id.reg_username);
        password_r = registerView.findViewById(R.id.reg_password);
        email_r = registerView.findViewById(R.id.reg_email);
        register = registerView.findViewById(R.id.register_btn);
        App context = (App) getApplication();
        userService = context.getUserService();
    }

}