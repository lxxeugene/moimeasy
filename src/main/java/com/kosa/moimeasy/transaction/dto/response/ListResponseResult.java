package com.kosa.moimeasy.transaction.dto.response;

import com.kosa.moimeasy.transaction.dto.response.ResponseResult;
import lombok.Getter;

import java.util.List;

@Getter
public class ListResponseResult<T> extends ResponseResult {

    private List<T> data;

    public ListResponseResult(List<T> data) {
        super(successResponse.statusCode, successResponse.messages, successResponse.developerMessage,
                successResponse.timestamp);
        this.data = data;
    }
}
