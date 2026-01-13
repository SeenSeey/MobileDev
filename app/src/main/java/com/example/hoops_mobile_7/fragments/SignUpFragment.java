package com.example.hoops_mobile_7.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.hoops_mobile_7.databinding.FragmentSignUpBinding;
import com.example.hoops_mobile_7.model.User;
import com.example.hoops_mobile_7.repository.sharedPrefs.UserPrefs;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.inpEmail.getText().toString().trim();
            String password = binding.inpPassword.getText().toString().trim();
            String age = binding.inpAge.getText().toString().trim();

            int checkedId = binding.genderGroup.getCheckedButtonId();
            String gender = null;
            if (checkedId == binding.btnMale.getId()) gender = "man";
            else if (checkedId == binding.btnFemale.getId()) gender = "woman";

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(age) || gender == null) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Неверный email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getContext(), "Пароль ≥ 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(email, password, age, gender);
            UserPrefs.saveUser(requireContext(), user);

            Bundle result = new Bundle();
            result.putString("email", email);
            getParentFragmentManager().setFragmentResult("signUpRequest", result);

            Toast.makeText(getContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();

            NavDirections action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment();
            Navigation.findNavController(v).navigate(action);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getParentFragmentManager().clearFragmentResultListener("signUpRequest");
        binding = null;
    }
}
