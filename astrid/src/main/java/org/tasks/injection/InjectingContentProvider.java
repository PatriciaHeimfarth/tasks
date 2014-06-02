package org.tasks.injection;

import android.content.ContentProvider;
import android.content.Context;

public abstract class InjectingContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Context context = getContext();
        ((Injector) context.getApplicationContext())
                .getObjectGraph()
                .plus(new ContentProviderModule(context))
                .inject(this);

        return true;
    }
}
