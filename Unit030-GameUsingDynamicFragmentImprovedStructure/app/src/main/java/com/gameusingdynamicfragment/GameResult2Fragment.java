package com.gameusingdynamicfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameResult2Fragment extends Fragment {

    private EditText mEdtCountSet,
                    mEdtCountPlayerWin,
                    mEdtCountComWin,
                    mEdtCountDraw;

    public GameResult2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 利用inflater物件的inflate()方法取得介面佈局檔，並將最後的結果傳回給系統
        return inflater.inflate(R.layout.fragment_game_result2, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mEdtCountSet = (EditText)getActivity().findViewById(R.id.edtCountSet);
        mEdtCountPlayerWin = (EditText)getActivity().findViewById(R.id.edtCountPlayerWin);
        mEdtCountComWin = (EditText)getActivity().findViewById(R.id.edtCountComWin);
        mEdtCountDraw = (EditText)getActivity().findViewById(R.id.edtCountDraw);

        ((MainActivity) getActivity()).mGameResultType = MainFragment.GameResultType.TYPE_2;
        ((MainActivity) getActivity()).fragResult = this;

        getActivity().findViewById(R.id.frameLay).setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.frameLay).setVisibility(View.GONE);
    }

    public void updateGameResult(int iCountSet,
                                 int iCountPlayerWin,
                                 int iCountComWin,
                                 int iCountDraw) {
        mEdtCountSet.setText(String.valueOf(iCountSet));
        mEdtCountDraw.setText(String.valueOf(iCountDraw));
        mEdtCountComWin.setText(String.valueOf(iCountComWin));
        mEdtCountPlayerWin.setText(String.valueOf(iCountPlayerWin));
    }
}
