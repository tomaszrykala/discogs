package com.tomaszrykala.discogs;

import com.tomaszrykala.discogs.dagger.component.AppComponent;

import javax.inject.Singleton;

import dagger.Component;

public class MockDiscogsApp extends DiscogsApp {

    @Singleton
    @Component(modules = MockMvpModule.class)
    public interface MockAppComponent extends AppComponent {
        void inject(AppJourneyTest appJourneyTest);
    }

    @Override public AppComponent getComponent() {
        return DaggerMockDiscogsApp_MockAppComponent.builder().mockMvpModule(new MockMvpModule()).build();
    }
}
