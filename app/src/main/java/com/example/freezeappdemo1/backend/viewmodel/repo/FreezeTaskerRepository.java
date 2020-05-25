package com.example.freezeappdemo1.backend.viewmodel.repo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.freezeappdemo1.backend.AppsDataBase;
import com.example.freezeappdemo1.backend.dao.FreezeTaskerDao;
import com.example.freezeappdemo1.backend.entitys.FreezeTasker;

import java.util.List;

public class FreezeTaskerRepository {

    private static FreezeTaskerRepository INSTANCE;
    LiveData<List<FreezeTasker>> listLiveDataFreezeTasker;

    public synchronized static FreezeTaskerRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FreezeTaskerRepository(context);
        }
        return INSTANCE;
    }

    FreezeTaskerDao freezeTaskerDao;
    AppsDataBase dataBase;

    private FreezeTaskerRepository(Context context) {
        dataBase = AppsDataBase.getDataBase(context);
        freezeTaskerDao = dataBase.getFreezeTaskerDao();
        this.listLiveDataFreezeTasker = freezeTaskerDao.getAllTasksLive();
    }

    public LiveData<List<FreezeTasker>> getListLiveDataFreezeTasker() {
        if (listLiveDataFreezeTasker == null) {
            listLiveDataFreezeTasker = new MutableLiveData<>();
        }
        return listLiveDataFreezeTasker;
    }

    public void insertFreezeTasker(FreezeTasker[] appsCategories) {
        new FreezeTaskerRepository.InsertAsyncTask(freezeTaskerDao).execute(appsCategories);
    }

    public void deleteFreezeTasker(FreezeTasker[] appsCategories) {
        new FreezeTaskerRepository.DeleteAsyncTask(freezeTaskerDao).execute(appsCategories);
    }

    public void updateFreezeTasker(FreezeTasker[] appsCategories) {
        new FreezeTaskerRepository.UpdateAsyncTask(freezeTaskerDao).execute(appsCategories);
    }

    public void deleteAllFreezeTasker() {
        new FreezeTaskerRepository.DeleteAllAsyncTask(freezeTaskerDao).execute();
    }

    public LiveData<List<FreezeTasker>> getListLiveDataFreezeByCategoryId(long id) {
        return freezeTaskerDao.getAllTasksLiveByCategoryId(id);
    }

    public static class InsertAsyncTask extends AsyncTask<FreezeTasker, Void, Void> {
        private FreezeTaskerDao freezeTaskerDao;

        public InsertAsyncTask(FreezeTaskerDao freezeTaskerDao) {
            this.freezeTaskerDao = freezeTaskerDao;
        }

        @Override
        protected Void doInBackground(FreezeTasker... tasks) {
            freezeTaskerDao.insertTasks(tasks);
            return null;
        }
    }


    public static class UpdateAsyncTask extends AsyncTask<FreezeTasker, Void, Void> {
        private FreezeTaskerDao freezeTaskerDao;

        public UpdateAsyncTask(FreezeTaskerDao freezeTaskerDao) {
            this.freezeTaskerDao = freezeTaskerDao;
        }

        @Override
        protected Void doInBackground(FreezeTasker... tasks) {
            freezeTaskerDao.updateTasks(tasks);
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<FreezeTasker, Void, Void> {
        private FreezeTaskerDao freezeTaskerDao;

        public DeleteAsyncTask(FreezeTaskerDao freezeTaskerDao) {
            this.freezeTaskerDao = freezeTaskerDao;
        }

        @Override
        protected Void doInBackground(FreezeTasker... tasks) {
            freezeTaskerDao.deleteTasks(tasks);
            return null;
        }
    }

    public  static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private FreezeTaskerDao freezeTaskerDao;

        public DeleteAllAsyncTask(FreezeTaskerDao freezeTaskerDao) {
            this.freezeTaskerDao = freezeTaskerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            freezeTaskerDao.deleteAllTasks();
            return null;
        }
    }

}
