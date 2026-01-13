package com.example.hoops_mobile_7.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.hoops_mobile_7.databinding.FragmentSignInBinding;
import com.example.hoops_mobile_7.model.User;
import com.example.hoops_mobile_7.repository.sharedPrefs.UserPrefs;

public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("signUpRequest", this, (requestKey, bundle) -> {
            String email = bundle.getString("email");
            if (email != null) {
                binding.inpEmail.setText(email);
                Toast.makeText(requireContext(), "Почта вставлена автоматически", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.inpEmail.getText().toString().trim();
            String password = binding.inpPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            User savedUser = UserPrefs.getUser(requireContext());
            NavDirections action;

            if (savedUser != null &&
                    email.equals(savedUser.getEmail()) &&
                    password.equals(savedUser.getPassword())) {

                action = SignInFragmentDirections.actionSignInFragmentToHomeFragment(savedUser);
                Navigation.findNavController(view).navigate(action);

            } else if (email.equals("test@test.com") && password.equals("123456")) {
                User user = new User(email, password, "0", "man");
                action = SignInFragmentDirections.actionSignInFragmentToHomeFragment(user);
                Navigation.findNavController(view).navigate(action);

            } else {
                Toast.makeText(requireContext(), "Неверные данные", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            NavDirections action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment();
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