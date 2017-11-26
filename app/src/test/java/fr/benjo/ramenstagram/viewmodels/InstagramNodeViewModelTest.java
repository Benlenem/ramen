package fr.benjo.ramenstagram.viewmodels;

import org.junit.Test;

import fr.benjo.ramenstagram.models.Node;
import fr.benjo.ramenstagram.utils.mvvm.Command1;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ben on 26/11/2017.
 */

public class InstagramNodeViewModelTest {

    @Test
    public void commandIsCalledWhenEnabled() {
        //given
        Node n = new Node("http://thumbnail.png", "http://full_size.png", "caption");
        Command1<InstagramNodeViewModel> commandMock = (Command1<InstagramNodeViewModel>) mock(Command1.class);
        when(commandMock.canExecute()).thenReturn(true);
        InstagramNodeViewModel vm = new InstagramNodeViewModel(n, commandMock);

        //when
        vm.onClick();

        //then
        verify(commandMock, times(1)).execute(vm);
    }

    @Test
    public void commandIsNotCalledWhenDisabled() {
        //given
        Node n = new Node("http://thumbnail.png", "http://full_size.png", "caption");
        Command1<InstagramNodeViewModel> commandMock = (Command1<InstagramNodeViewModel>) mock(Command1.class);
        when(commandMock.canExecute()).thenReturn(false);
        InstagramNodeViewModel vm = new InstagramNodeViewModel(n, commandMock);

        //when
        vm.onClick();

        //then
        verify(commandMock, times(0)).execute(vm);
    }
}
