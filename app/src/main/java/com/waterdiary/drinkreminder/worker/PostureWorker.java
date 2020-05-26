package com.waterdiary.drinkreminder.worker;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class PostureWorker extends Worker {


    public PostureWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Call a function I want to execute.

        return Result.success();
    }

    // TODO: Put classifer
    private void postureClassfier(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){


        }
    }
}
