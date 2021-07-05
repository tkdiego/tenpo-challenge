package com.taka.tenpo.domain.recorder.service;

import com.taka.tenpo.domain.recorder.model.HistoryRequest;
import com.taka.tenpo.domain.recorder.model.PaginationResponse;
import com.taka.tenpo.domain.recorder.model.TrackingRecorder;

public interface IRecorderService {

    void recordTracking(TrackingRecorder trackingRecorder);

    PaginationResponse<TrackingRecorder> getAllRecordTracking(HistoryRequest historyRequest);
}
