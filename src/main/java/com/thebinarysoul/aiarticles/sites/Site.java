package com.thebinarysoul.aiarticles.sites;

import com.thebinarysoul.aiarticles.Article;
import java.util.ArrayList;
import java.util.List;

public abstract class Site {

   protected List<Article> list = new ArrayList<Article>();

   public abstract List<Article> getArticles();

}
