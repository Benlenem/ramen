package fr.benjo.ramenstagram.viewmodels;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ben on 26/11/2017.
 */

public class FullscreenImageViewModelTest {

    @Test
    public void fieldsAreSetWhenViewModelIsCreated() {
        //when
        FullscreenImageViewModel vm = new FullscreenImageViewModel("ping", "pong", 0);

        //then
        assertEquals(vm.caption.get(), "ping");
        assertEquals(vm.pictureUrl.get(), "pong");
    }
}
