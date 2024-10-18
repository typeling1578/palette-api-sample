package com.example.paletteapisample;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.palette.graphics.Palette;

import com.example.paletteapisample.databinding.FragmentFirstBinding;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
                }

        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ImageView imgView = binding.imageView;
            BufferedInputStream inputStream = null;
            Bitmap bitmap = null;
            try {
                inputStream = new BufferedInputStream(getContext().getContentResolver().openInputStream(data.getData()));
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage("File not found");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User taps OK button.
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return;
            }

            try {
                binding.colors.removeAllViews();
                binding.result.setText("");

                UsePalette p = new UsePalette(bitmap);
                List<Palette.Swatch> swatches = p.getColors();
                for (Palette.Swatch swatch : swatches) {
                    @ColorInt int color = swatch.getRgb();
                    Button button = new Button(getContext());
                    button.setText(String.valueOf(swatch.getPopulation()));
                    button.setTextColor(swatch.getTitleTextColor());
                    button.setBackgroundColor(color);
                    binding.colors.addView(button);

                    binding.result.append(swatch.toString() + ",\n");
                }
            } catch (Exception e) {
                e.printStackTrace();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage(e.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User taps OK button.
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return;
            }
        }
    }

}