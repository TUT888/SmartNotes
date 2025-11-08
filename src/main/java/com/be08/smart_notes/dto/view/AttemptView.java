package com.be08.smart_notes.dto.view;

public interface AttemptView {
    public interface Basic {}
    public interface Detail extends AttemptView.Basic {}
    public interface Answer extends AttemptView.Detail {}
}
