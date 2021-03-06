package com.taka.tenpo.controller;


import com.taka.tenpo.domain.recorder.aspect.TrackingRequest;
import com.taka.tenpo.domain.recorder.enums.RequestType;
import com.taka.tenpo.domain.recorder.model.HistoryRequest;
import com.taka.tenpo.domain.recorder.model.PaginationResponse;
import com.taka.tenpo.domain.recorder.model.TrackingRecorder;
import com.taka.tenpo.domain.recorder.service.IRecorderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.taka.tenpo.controller.URLConstants.API;
import static com.taka.tenpo.controller.URLConstants.HISTORY_DEFAULT_PAGE;
import static com.taka.tenpo.controller.URLConstants.HISTORY_DEFAULT_PAGE_LIMIT;
import static com.taka.tenpo.controller.URLConstants.REQUEST_TYPE;
import static com.taka.tenpo.controller.URLConstants.TRACKING_HISTORY;

@RestController
@RequestMapping(API)
@AllArgsConstructor
public class TrackingHistoryController {

    private final IRecorderService recorderService;

    @GetMapping(TRACKING_HISTORY)
    @ResponseBody
    @TrackingRequest(requestType = RequestType.TRACKING_HISTORY)
    public PaginationResponse<TrackingRecorder> getHistory(@RequestParam(required = false, name = REQUEST_TYPE) RequestType requestType,
                                                           @RequestParam(required = false, defaultValue = HISTORY_DEFAULT_PAGE) int page,
                                                           @RequestParam(required = false, defaultValue = HISTORY_DEFAULT_PAGE_LIMIT) int limit) {

        return recorderService.getAllRecordTracking(new HistoryRequest(requestType, page, limit));
    }
}
