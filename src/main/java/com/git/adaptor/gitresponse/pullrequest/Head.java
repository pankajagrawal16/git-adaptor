
package com.git.adaptor.gitresponse.pullrequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "label",
    "ref",
    "sha",
    "user",
    "repo"
})
public class Head {

    @JsonProperty("label")
    public String label;
    @JsonProperty("ref")
    public String ref;
    @JsonProperty("sha")
    public String sha;
    @JsonProperty("user")
    public User_ user;
    @JsonProperty("repo")
    public Repo repo;

}
