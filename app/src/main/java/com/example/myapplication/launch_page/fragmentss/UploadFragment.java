package com.example.myapplication.launch_page.fragmentss;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.myapplication.R;
import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.Retrofitclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UploadFragment extends Fragment {
    private EditText bookname, authname, btechy, smalldesc, detaileddesc;
    private Spinner schoolSpinner;
    private Button submitButton;
    private CardView uploadButton;
    private String selectedSchool;
    private Uri pdfUri;

    private static final int PICK_PDF_REQUEST = 1;

    public UploadFragment() {
        // Required empty public constructor
    }

    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        // Initialize Views
        schoolSpinner = view.findViewById(R.id.school);
        bookname = view.findViewById(R.id.email1);
        authname = view.findViewById(R.id.phonenumber);
        btechy = view.findViewById(R.id.semester2);
        smalldesc = view.findViewById(R.id.present1);
        detaileddesc = view.findViewById(R.id.permanent1);
        uploadButton = view.findViewById(R.id.upload_avatar);
        submitButton = view.findViewById(R.id.Submit); // Add this button in XML

        // Set up Spinner
        List<String> schoolList = Arrays.asList("School Of Computing", "School Of Electronics", "School Of Basic Science");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, schoolList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(adapter);

        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSchool = schoolList.get(position);
                Toast.makeText(requireContext(), "Selected: " + selectedSchool, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Handle PDF Upload
        uploadButton.setOnClickListener(v -> openFileChooser());

        // Handle Form Submission
        submitButton.setOnClickListener(v -> submitForm());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            Toast.makeText(requireContext(), "PDF Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitForm() {
        String bname = bookname.getText().toString().trim();
        String aname = authname.getText().toString().trim();
        String yr = btechy.getText().toString().trim();
        String sdesc = smalldesc.getText().toString().trim();
        String detdesc = detaileddesc.getText().toString().trim();

        if (bname.isEmpty() || aname.isEmpty() || yr.isEmpty() || sdesc.isEmpty() || detdesc.isEmpty() || pdfUri == null) {
            Toast.makeText(requireContext(), "Please fill all fields and select a PDF", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the upload function here
        uploadPDF(bname, aname, yr, sdesc, detdesc);
    }

    private void uploadPDF(String title, String author, String year, String smallDesc, String detailedDesc) {
        if (pdfUri == null) {
            Toast.makeText(requireContext(), "No PDF selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(pdfUri);
            byte[] pdfBytes = new byte[inputStream.available()];
            inputStream.read(pdfBytes);
            inputStream.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), pdfBytes);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "uploaded_file.pdf", requestFile);

            RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
            RequestBody authorBody = RequestBody.create(MediaType.parse("text/plain"), author);
            RequestBody fieldBody = RequestBody.create(MediaType.parse("text/plain"), selectedSchool);
            RequestBody isbnBody = RequestBody.create(MediaType.parse("text/plain"), "1234567890");
            RequestBody semesterBody = RequestBody.create(MediaType.parse("text/plain"), year);
            RequestBody smallDescBody = RequestBody.create(MediaType.parse("text/plain"), smallDesc);
            RequestBody detailedDescBody = RequestBody.create(MediaType.parse("text/plain"), detailedDesc);

            ApiService apiService = Retrofitclient.getInstance().create(ApiService.class);
            Call<ResponseBody> call = apiService.uploadBook(filePart, titleBody, authorBody, fieldBody, isbnBody, semesterBody, smallDescBody, detailedDescBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Upload Successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            if (errorBody.contains("construct")) {
                                Toast.makeText(requireContext(), "Upload Successful!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(requireContext(), "Upload Failed: " + errorBody, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Upload Failed: Unknown Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to read file!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = requireContext().getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }


}
