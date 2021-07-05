package com.taka.tenpo.domain.recorder.repository;

import com.taka.tenpo.domain.recorder.enums.RequestType;
import com.taka.tenpo.domain.recorder.model.TrackingRecorder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrackingRecorderRepository extends PagingAndSortingRepository<TrackingRecorder, Integer> {

    Page<TrackingRecorder> findByRequestType(RequestType requestType, Pageable pageable);
}
