package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

public abstract class Site {

   protected List<Article> list = new ArrayList<Article>();

   public abstract List<Article> getArticles();

}
