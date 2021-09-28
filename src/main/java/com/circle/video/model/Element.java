package com.circle.video.model;

public enum Element {

  ITEMS_PER_PAGE((short)8), VOTE_VALUE((short)1);

  private final short value;

  Element(short value) {
    this.value = value;
  }


  public short getValue(){
    return value;
  }
}
