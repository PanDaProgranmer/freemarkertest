package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by panda on 2018/3/14.
 */
@Service
public class TestService {
    public List<Map> test(Map map) {
        List<Map> list = new ArrayList<>();
        list.add(map);
        return list;
    }
}
