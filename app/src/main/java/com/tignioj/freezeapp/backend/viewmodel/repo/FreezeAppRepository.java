package com.tignioj.freezeapp.backend.viewmodel.repo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tignioj.freezeapp.backend.AppsDataBase;
import com.tignioj.freezeapp.backend.dao.FreezeAppDao;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;

import java.util.List;

public class FreezeAppRepository {

    private Context mContext;
    private static FreezeAppRepository INSTANCE;

    public synchronized static FreezeAppRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FreezeAppRepository(context);
        }
        return INSTANCE;
    }

    FreezeAppDao freezeAppDao;
    AppsDataBase dataBase;

    private FreezeAppRepository(Context context) {
        dataBase = AppsDataBase.getDataBase(context);
        freezeAppDao = dataBase.getFreezeAppDao();
        this.mContext = context;
    }



    public List<FreezeApp> getFreezeAppByCategoryId(long id) {
        return freezeAppDao.getAllAppsByCategoryId(id);
    }

    public void insertFreezeApp(FreezeApp ...freezeApps) {
        new FreezeAppRepository.InsertAsyncTask(freezeAppDao).execute(freezeApps);
    }

    public void deleteFreezeApp(FreezeApp ...freezeApps) {
        new FreezeAppRepository.DeleteAsyncTask(freezeAppDao).execute(freezeApps);
    }

    public void updateFreezeApp(FreezeApp ...freezeApps) {
        new FreezeAppRepository.UpdateAsyncTask(freezeAppDao).execute(freezeApps);
    }

    public void deleteAllFreezeApp() {
        new FreezeAppRepository.DeleteAllAsyncTask(freezeAppDao).execute();
    }

    public LiveData<List<FreezeApp>> getFreezeAppLiveByCategoryId(long categoryId) {
        return freezeAppDao.getAllAppsLiveByCategoryId(categoryId);
    }

    public List<FreezeApp> getAllFrozenApps() {
        return freezeAppDao.getAllFrozenApps();
    }

    public LiveData<List<FreezeApp>> getMutableLiveDataFreezeAppListLiveInCategoryWithPattern(long categoryId, String pattern) {
        return freezeAppDao.getMutableLiveDataFreezeAppListLiveInCategoryWithPattern(categoryId, "%" + pattern + "%");
    }

    public FreezeApp getFreezeAppByPackageName(String packageName) {
        return freezeAppDao.getFreezeAppByPackageName(packageName);
    }

    /**
     * 把数据库中所有冻结的改为未冻结
     */
    public void unFreezeAllApp() {
        List<FreezeApp> allFrozenApps = getAllFrozenApps();
        for (FreezeApp f : allFrozenApps) {
            f.setFrozen(false);
            updateFreezeApp(f);
        }
    }


    public static class InsertAsyncTask extends AsyncTask<FreezeApp, Void, Void> {
        private FreezeAppDao freezeAppDao;

        public InsertAsyncTask(FreezeAppDao freezeAppDao) {
            this.freezeAppDao = freezeAppDao;
        }

        @Override
        protected Void doInBackground(FreezeApp... tasks) {
            freezeAppDao.insertApps(tasks);
            return null;
        }
    }


    public static class UpdateAsyncTask extends AsyncTask<FreezeApp, Void, Void> {
        private FreezeAppDao freezeAppDao;

        public UpdateAsyncTask(FreezeAppDao freezeAppDao) {
            this.freezeAppDao = freezeAppDao;
        }

        @Override
        protected Void doInBackground(FreezeApp... tasks) {
            freezeAppDao.updateApps(tasks);
            return null;
        }
    }


    public static class DeleteAsyncTask extends AsyncTask<FreezeApp, Void, Void> {
        private FreezeAppDao freezeAppDao;

        public DeleteAsyncTask(FreezeAppDao freezeAppDao) {
            this.freezeAppDao = freezeAppDao;
        }

        @Override
        protected Void doInBackground(FreezeApp... tasks) {
            freezeAppDao.deleteApps(tasks);
            return null;
        }
    }

    public static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private FreezeAppDao freezeAppDao;

        public DeleteAllAsyncTask(FreezeAppDao freezeAppDao) {
            this.freezeAppDao = freezeAppDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            freezeAppDao.deleteAllFreezeApps();
            return null;
        }
    }
}
