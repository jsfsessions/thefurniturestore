package com.web.fileprocessing;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ProgressBarBean implements Serializable {

    private static final long serialVersionUID = 5297529526920866432L;

    private final BarIncrementator barIncrementator;

    public ProgressBarBean() {
        barIncrementator = new BarIncrementator();
    }

    private Integer progress;

    public Integer getProgress() {

        System.out.println("Progress bar updating time ...");

        if (progress == null) {
            progress = 0;
        } else {
            if (progress > 100) {
                progress = 100;
            } else {
                progress = barIncrementator.getValue();
            }
        }

        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public BarIncrementator getBarIncrementator() {
        return barIncrementator;
    }

    public void onComplete() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Process Complete"));
    }

    public void cancel() {
        progress = null;
    }
}
