package com.example.masha.photoalbumapp52;

import java.io.Serializable;

/**
 * Created by Masha on 5/1/2016.
 */

public class Tag implements Serializable {

    public String tagType;
    public String tagValue;
    public Tag(String tagType, String tagValue) {
        this.tagType = tagType;
        this.tagValue = tagValue;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Tag)) {
            return false;
        } else {
            Tag tag = ((Tag) o);

            if ((this.tagType.equals(tag.tagType) || this.tagType.startsWith(tag.tagType))
                    && (this.tagValue.equals(tag.tagValue) || this.tagValue.startsWith(tag.tagValue)) ){
                return true;
            } else {
                return false;
            }
        }
    }

    public String toString() {
        return this.tagType + ":" + tagValue;
    }
}
