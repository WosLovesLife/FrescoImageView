package com.example.leonard.frescoimageview;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class ImagePickerFragment extends Fragment {
    public static final String KEY_PICTURES_TAKEN = "picturesTaken";
    private ImagePickerRecyclerviewAdapter adapter;

    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> picturesTaken;

    private String curentFolder;
    private AppCompatActivity mActivity;
    private RecyclerView mRecyclerView;

    public static ImagePickerFragment newInstance() {

        Bundle args = new Bundle();

        ImagePickerFragment fragment = new ImagePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picturesTaken = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupActionBar();
        setupRecyclerView();

        curentFolder = getString(R.string.l_album_name_all);
        getImages(curentFolder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putSerializable(KEY_PICTURES_TAKEN, picturesTaken);
        }
    }

    private void setupActionBar() {
        spinnerAdapter = new ArrayAdapter<>(mActivity, R.layout.spinner_item, new ArrayList<String>());
        spinnerAdapter.add(getString(R.string.l_album_name_all));
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String folderName = (String) parent.getItemAtPosition(position);

                if (!curentFolder.contentEquals(folderName)) {
                    getImages(folderName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button done = (Button) getView().findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 17/8/11
            }
        });

    }

    private void setupRecyclerView() {
        RecyclerView recyclerview = mRecyclerView;

        ArrayList<String> images = new ArrayList<>();
        final GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 3);

        adapter = new ImagePickerRecyclerviewAdapter(mActivity, images);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new SpaceDecorator(18));
        recyclerview.setLayoutManager(layoutManager);
    }

    private void getImages(String folder) {
        ContentResolver cr = mActivity.getContentResolver();
        String[] columns = new String[]{
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.SIZE};
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                MediaStore.Images.Media.DATA + " like ? and " + MediaStore.Images.ImageColumns.SIZE + " <> ?",
                new String[]{"%" + (!folder.contentEquals(getString(R.string.l_album_name_all)) ? "/" + folder + "/" : "") + "%", "0"},
                MediaStore.MediaColumns.DATE_ADDED + " DESC");

        curentFolder = folder;
        if (cursor != null) {
            adapter.clear();
            while (cursor.moveToNext()) {
                adapter.add(cursor.getString(1));
                String[] tree = cursor.getString(1).split("/");
                String folderName = tree[tree.length - 2];

                if (spinnerAdapter.getPosition(folderName) < 0) {
                    spinnerAdapter.add(folderName);
                    spinnerAdapter.notifyDataSetChanged();
                }
            }
            cursor.close();
        }
    }
}
