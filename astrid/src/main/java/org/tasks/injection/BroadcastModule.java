package org.tasks.injection;

import android.content.Context;

import com.todoroo.astrid.alarms.AlarmDetailExposer;
import com.todoroo.astrid.alarms.AlarmTaskRepeatListener;
import com.todoroo.astrid.calls.PhoneStateChangedReceiver;
import com.todoroo.astrid.core.CustomFilterExposer;
import com.todoroo.astrid.gcal.GCalTaskCompleteListener;
import com.todoroo.astrid.gtasks.GtasksCustomFilterCriteriaExposer;
import com.todoroo.astrid.gtasks.GtasksDetailExposer;
import com.todoroo.astrid.gtasks.GtasksFilterExposer;
import com.todoroo.astrid.gtasks.GtasksStartupReceiver;
import com.todoroo.astrid.notes.NotesDetailExposer;
import com.todoroo.astrid.reminders.Notifications;
import com.todoroo.astrid.repeats.RepeatDetailExposer;
import com.todoroo.astrid.repeats.RepeatTaskCompleteListener;
import com.todoroo.astrid.service.GlobalEventReceiver;
import com.todoroo.astrid.tags.TagCustomFilterCriteriaExposer;
import com.todoroo.astrid.tags.TagDetailExposer;
import com.todoroo.astrid.tags.TagFilterExposer;
import com.todoroo.astrid.timers.TimerFilterExposer;
import com.todoroo.astrid.timers.TimerTaskCompleteListener;
import com.todoroo.astrid.widget.TasksWidget;

import org.tasks.scheduling.RefreshBroadcastReceiver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {
        RefreshBroadcastReceiver.class,
        TasksWidget.class,
        Notifications.class,
        GtasksCustomFilterCriteriaExposer.class,
        GtasksDetailExposer.class,
        GlobalEventReceiver.class,
        TagDetailExposer.class,
        TagCustomFilterCriteriaExposer.class,
        NotesDetailExposer.class,
        GCalTaskCompleteListener.class,
        RepeatDetailExposer.class,
        TimerTaskCompleteListener.class,
        RepeatTaskCompleteListener.class,
        AlarmTaskRepeatListener.class,
        AlarmDetailExposer.class,
        GtasksStartupReceiver.class,
        PhoneStateChangedReceiver.class,
        TimerFilterExposer.class,
        CustomFilterExposer.class,
        GtasksFilterExposer.class,
        TagFilterExposer.class
})
public class BroadcastModule {

    private final Context context;

    public BroadcastModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    @ForApplication
    public Context getContext() {
        return context.getApplicationContext();
    }
}
