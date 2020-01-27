package com.example.appsdata;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsdata.Api.RequestInterface;
import com.example.appsdata.Api.ServerRequest;
import com.example.appsdata.Api.ServerResponse;
import com.example.appsdata.constants.Constants;
import com.example.appsdata.model.User;

import customfonts.MyEditText;
import customfonts.MyTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    private Button Login;
    private MyEditText edtEmail, edtPassword;
    private TextView register;
    private ProgressBar progressBar;
    private SharedPreferences pref;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        pref = getActivity().getPreferences(0);

        Login = view.findViewById(R.id.btnLogin);
        edtEmail = view.findViewById(R.id.email);
        edtPassword = view.findViewById(R.id.password);
        register = view.findViewById(R.id.create);

        progressBar = view.findViewById(R.id.progress);


        Login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                goToRegister();
                break;

            case R.id.btnLogin:
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    loginProcess(email, password);
                } else {
                    Toast.makeText(getContext(), "Kolom tidak boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void loginProcess(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> responseCall = requestInterface.operation(request);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();

                if (resp.getResult().equals(Constants.SUCCESS)) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGIN_IN, true);
                    editor.putString(Constants.EMAIL, resp.getUser().getEmail());
                    editor.putString(Constants.NAME, resp.getUser().getName());
                    editor.putString(Constants.UNIQUE_ID, resp.getUser().getUnique_id());
                    editor.apply();
                    goToMenu();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, "failed");
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMenu() {
        Fragment menumain = new MainMenuFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, menumain);
        ft.commit();

    }

    private void goToRegister() {
        Fragment register = new SignUpFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, register);
        ft.commit();
    }
}
