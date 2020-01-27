package com.example.appsdata;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsdata.Api.RequestInterface;
import com.example.appsdata.Api.ServerRequest;
import com.example.appsdata.Api.ServerResponse;
import com.example.appsdata.constants.Constants;
import com.example.appsdata.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    Button signup;
    EditText edtUsername, edtEmail, edtPassword;
    private ProgressBar progressBar;
    TextView masuk;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        signup = view.findViewById(R.id.btnSignUp);
        edtUsername = view.findViewById(R.id.txtUsername);
        edtEmail = view.findViewById(R.id.txtEmail);
        edtPassword = view.findViewById(R.id.txtPassword);
        masuk = view.findViewById(R.id.txtMasuk);

        progressBar = view.findViewById(R.id.progressSignUp);

        signup.setOnClickListener(this);
        masuk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtMasuk:
                goToLogin();
                break;


            case R.id.btnSignUp:

                String name = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    registerProcess(name,email,password);
                }else {
                    Toast.makeText(getContext(), "Isian tidak boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void registerProcess(String name,String email,String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user   = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);
        Call<ServerResponse> responseCall = requestInterface.operation(request);

        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse response1 = response.body();
                Toast.makeText(getContext(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"Failed");
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void goToLogin(){
        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }
}
