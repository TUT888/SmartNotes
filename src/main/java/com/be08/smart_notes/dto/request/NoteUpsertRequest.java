package com.be08.smart_notes.dto.request;

import com.be08.smart_notes.validation.group.OnCreate;
import com.be08.smart_notes.validation.group.OnUpdate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteUpsertRequest {
    @NotNull(groups = OnCreate.class)
    private Integer userId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private String title;
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private String content;
}