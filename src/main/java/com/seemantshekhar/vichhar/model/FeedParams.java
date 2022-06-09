package com.seemantshekhar.vichhar.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedParams {

    protected Integer offset;
    protected Integer limit;

    @AssertTrue
    protected boolean getValidPage() {
        return (offset != null && limit != null) || (offset == null && limit == null);
    }
}
