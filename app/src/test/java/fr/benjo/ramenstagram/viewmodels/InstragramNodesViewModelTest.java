package fr.benjo.ramenstagram.viewmodels;

import android.support.annotation.NonNull;

import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;

import fr.benjo.ramenstagram.RxImmediateSchedulerRule;
import fr.benjo.ramenstagram.api.InstagramApi;
import fr.benjo.ramenstagram.models.Media;
import fr.benjo.ramenstagram.models.Node;
import fr.benjo.ramenstagram.models.Tag;
import fr.benjo.ramenstagram.models.TagDetail;
import fr.benjo.ramenstagram.utils.mvvm.Command;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ben on 26/11/2017.
 */

public class InstragramNodesViewModelTest {

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Test
    public void apiIsCalledWhenViewModelIsCreated() {
        //given
        InstagramApi instagramApiMock = mock(InstagramApi.class);
        when(instagramApiMock.getTagDetail("ramen")).thenReturn(Observable.empty());

        //when
        InstagramNodesViewModel vm = new InstagramNodesViewModel(instagramApiMock, "ramen", Command.EMPTY_COMMAND1);

        //then
        verify(instagramApiMock, times(1)).getTagDetail("ramen");
    }

    @Test
    public void apiIsCalledWhenRefreshIsAsked() {
        //given
        InstagramApi instagramApiMock = mock(InstagramApi.class);
        when(instagramApiMock.getTagDetail("ramen")).thenReturn(Observable.empty());

        //when
        InstagramNodesViewModel vm = new InstagramNodesViewModel(instagramApiMock, "ramen", Command.EMPTY_COMMAND1);
        vm.onRefresh();

        //then
        verify(instagramApiMock, times(2)).getTagDetail("ramen");
    }

    @Test
    public void listIsFilledWhenApiResponds() {
        //given
        InstagramApi instagramApiMock = mock(InstagramApi.class);
        TagDetail tagDetail = getTagDetail();
        when(instagramApiMock.getTagDetail("ramen")).thenReturn(Observable.just(tagDetail));

        //when
        InstagramNodesViewModel vm = new InstagramNodesViewModel(instagramApiMock, "ramen", Command.EMPTY_COMMAND1);

        //then
        assertEquals(vm.ramenList.size(), 2);
    }

    @Test
    public void listIsEmptyWhenApiSendsError() {
        //given
        InstagramApi instagramApiMock = mock(InstagramApi.class);
        TagDetail tagDetail = getTagDetail();
        when(instagramApiMock.getTagDetail("ramen")).thenReturn(Observable.error(new Exception("http error")));

        //when
        InstagramNodesViewModel vm = new InstagramNodesViewModel(instagramApiMock, "ramen", Command.EMPTY_COMMAND1);

        //then
        assertEquals(vm.ramenList.size(), 0);
    }

    @Test
    public void layoutModeIsListWhenViewModelIsCreated() {
        //given
        InstagramApi instagramApiMock = mock(InstagramApi.class);
        when(instagramApiMock.getTagDetail("ramen")).thenReturn(Observable.empty());

        //when
        InstagramNodesViewModel vm = new InstagramNodesViewModel(instagramApiMock, "ramen", Command.EMPTY_COMMAND1);

        //then
        assertEquals(vm.layoutMode.get(), InstagramNodesViewModel.LAYOUT_MODE_LIST);
    }

    @Test
    public void layoutModeIsGridWhenToggled() {
        //given
        InstagramApi instagramApiMock = mock(InstagramApi.class);
        when(instagramApiMock.getTagDetail("ramen")).thenReturn(Observable.empty());

        //when
        InstagramNodesViewModel vm = new InstagramNodesViewModel(instagramApiMock, "ramen", Command.EMPTY_COMMAND1);
        vm.onToggleMode();

        //then
        assertEquals(vm.layoutMode.get(), InstagramNodesViewModel.LAYOUT_MODE_GRID);
    }

    @NonNull
    private TagDetail getTagDetail() {
        Media media = new Media(Arrays.asList(
                new Node("http://thumbnail.png", "http://full_size.png", "caption"),
                new Node("http://thumbnail.png", "http://full_size.png", "caption")
        ));
        Tag tag = new Tag(media);
        return new TagDetail(tag);
    }
}
