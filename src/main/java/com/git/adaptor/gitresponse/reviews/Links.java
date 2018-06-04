
package com.git.adaptor.gitresponse.reviews;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "html",
    "pull_request"
})
public class Links {

    @JsonProperty("html")
    public Html html;
    @JsonProperty("pull_request")
    public PullRequest pullRequest;

}
