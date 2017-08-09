package com.itr.outlet.walmart.control;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ProgressBarViewBean implements Serializable {

    private final BarIncrementator barIncrementator;

    public ProgressBarViewBean() {
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
