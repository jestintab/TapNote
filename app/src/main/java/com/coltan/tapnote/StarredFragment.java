package com.coltan.tapnote;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coltan.tapnote.db.DatabaseHandler;
import com.coltan.tapnote.db.Note;

import java.util.ArrayList;
import java.util.List;

public class StarredFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SearchView.OnQueryTextListener {

    private ArrayList<String> mId = new ArrayList<>();
    private ArrayList<String> mHeader = new ArrayList<>();
    private ArrayList<String> mSubHeader = new ArrayList<>();
    private ArrayList<String> mTag = new ArrayList<>();
    private ArrayList<String> mNote = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MenuItem searchItem;
    private SearchView searchView;
    private static LinearLayout noResults;

    public StarredFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_starred, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        noResults = (LinearLayout) v.findViewById(R.id.noResults);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        initData();

        RecyclerView.Adapter mAdapter = new NoteAdapter(mHeader, mSubHeader, mTag, 1);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddNoteActivity.class));
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));

        return v;
    }

    @Override
    public void onItemClick(View childView, int position) {
        // Do something when an item is clicked.
        int id = Integer.parseInt(mId.get(position));
        Intent i = new Intent(getActivity(), EditNoteActivity.class);
        i.putExtra("id", id);
        i.putExtra("position", position);
        startActivity(i);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        // Do another thing when an item is long pressed.
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(mHeader.get(position), mNote.get(position));
        clipboard.setPrimaryClip(clip);
        Snackbar.make(childView, "Copied to clipboard", Snackbar.LENGTH_SHORT).show();
    }

    private void initData() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        List<Note> notes = db.getAllStarredNotes();
        for (Note nt : notes) {
            mId.add(String.valueOf(nt.getID()));
            mHeader.add(nt.getTitle());
            String note = nt.getNote();
            mNote.add(note);
            note = note.replace("\n", " ");
            String cnote;
            if (note.length() > 25) {
                cnote = note.substring(0, 25);
                cnote = cnote.concat("...");
            } else {
                cnote = note;
            }
            mSubHeader.add(cnote);
            mTag.add(nt.getTag());
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            ((NoteAdapter) mRecyclerView.getAdapter()).getFilter().filter("");
        } else {
            ((NoteAdapter) mRecyclerView.getAdapter()).getFilter().filter(newText);
        }

        return false;
    }

    public static void setResultsMessage(Boolean result) {
        if (result) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    if (searchItem.isVisible() && !searchView.isIconified()) {
                        searchView.onActionViewCollapsed();
                    } else {
                        getActivity().finish();
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
