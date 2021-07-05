package com.taka.tenpo.domain.recorder.service;


import com.taka.tenpo.domain.recorder.model.HistoryRequest;
import com.taka.tenpo.domain.recorder.model.PaginationResponse;
import com.taka.tenpo.domain.recorder.model.TrackingRecorder;
import com.taka.tenpo.domain.recorder.repository.TrackingRecorderRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;


@Service
@AllArgsConstructor
public class RecorderService implements IRecorderService {

    private static final Logger LOGGER = getLogger(RecorderService.class);

    private final TrackingRecorderRepository trackingRecorderRepository;

    @Override
    @Async
    public void recordTracking(TrackingRecorder trackingRecorder) {
        trackingRecorderRepository.save(trackingRecorder);
        LOGGER.info("Tracking Record was successfully. {}", trackingRecorder.toString());
    }

    @Override
    public PaginationResponse<TrackingRecorder> getAllRecordTracking(HistoryRequest historyRequest) {
        Pageable pageable = PageRequest.of(historyRequest.getPage(), historyRequest.getPageSize());
        Page<TrackingRecorder> page;
        if (historyRequest.getRequestType() != null) {
            page = trackingRecorderRepository.findByRequestType(historyRequest.getRequestType(), pageable);
        } else {
            page = trackingRecorderRepository.findAll(pageable);
        }
        return new PaginationResponse<TrackingRecorder>(page.getContent(), page.getTotalPages(), page.getPageable().getPageNumber(), (int) page.getTotalElements());
    }
}
