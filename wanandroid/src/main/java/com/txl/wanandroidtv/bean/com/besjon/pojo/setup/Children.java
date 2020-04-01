/**
  * Copyright 2020 bejson.com 
  */
package com.txl.wanandroidtv.bean.com.besjon.pojo.setup;
import java.util.List;

/**
 * Auto-generated: 2020-04-01 18:22:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Children {

    private List<String> children;
    private int courseId;
    private int id;
    private String name;
    private int order;
    private int parentChapterId;
    private boolean userControlSetTop;
    private int visible;
    public void setChildren(List<String> children) {
         this.children = children;
     }
     public List<String> getChildren() {
         return children;
     }

    public void setCourseId(int courseId) {
         this.courseId = courseId;
     }
     public int getCourseId() {
         return courseId;
     }

    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setOrder(int order) {
         this.order = order;
     }
     public int getOrder() {
         return order;
     }

    public void setParentChapterId(int parentChapterId) {
         this.parentChapterId = parentChapterId;
     }
     public int getParentChapterId() {
         return parentChapterId;
     }

    public void setUserControlSetTop(boolean userControlSetTop) {
         this.userControlSetTop = userControlSetTop;
     }
     public boolean getUserControlSetTop() {
         return userControlSetTop;
     }

    public void setVisible(int visible) {
         this.visible = visible;
     }
     public int getVisible() {
         return visible;
     }

}