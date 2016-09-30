package com.nathb.swiftshop.task;

import android.os.AsyncTask;

import io.realm.Realm;

public abstract class RealmTask extends AsyncTask<Void, Void, Void> {

    protected abstract Realm.Transaction createTransaction();

    protected void onComplete() { }

    @Override
    protected Void doInBackground(Void... params) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(createTransaction());
        realm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        onComplete();
    }
}
