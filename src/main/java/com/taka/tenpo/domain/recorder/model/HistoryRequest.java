package com.taka.tenpo.domain.recorder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HistoryRequest {

    private RequestType requestType;
    private int page;
    private int pageSize;

}
