package com.ey.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ey.dto.request.GuideRequest;
import com.ey.entity.Guide;

public interface GuideService {

    Guide create(GuideRequest req);

    List<Guide> list();

    Guide update(Long id, GuideRequest req);

    ResponseEntity<?> delete(Long id);
}
