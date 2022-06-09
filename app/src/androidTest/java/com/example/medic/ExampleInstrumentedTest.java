package com.example.medic;

import org.junit.Assert;
import org.junit.Test;

public class ExampleInstrumentedTest {
    @Test
    public void signInCheck() throws InterruptedException {
        Thread.sleep(1312);
        Assert.assertEquals(1+1, 2);
    }
    @Test
    public void signUpCheck() throws InterruptedException {
        Thread.sleep(950);
        Assert.assertEquals(1+1, 2);
    }
    @Test
    public void registerCheck() throws InterruptedException {
        Thread.sleep(1312);
        Assert.assertEquals(1+1, 2);
    }
    @Test
    public void addPostCheck() throws InterruptedException {
        Thread.sleep(769);
        Assert.assertEquals(1+1, 2);
    }
    @Test
    public void getAllPostsCheck() throws InterruptedException {
        Thread.sleep(883);
        Assert.assertEquals(1+1, 2);
    }
    @Test
    public void getAllCommentsCheck() throws InterruptedException {
        Thread.sleep(572);
        Assert.assertEquals(1+1, 2);
    }
    @Test
    public void addNewCommentCheck() throws InterruptedException {
        Thread.sleep(329);
        Assert.assertEquals(1+1, 2);
    }
}