package com.oneside.ui.photopicker;

import android.support.v7.widget.RecyclerView;

import com.oneside.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {


    protected List<PhotoDirectory> photoDirectories;
    protected List<Photo> selectedPhotos;
    public int currentDirectoryIndex = 0;


    public SelectableAdapter() {
        photoDirectories = new ArrayList<PhotoDirectory>();
        selectedPhotos = new ArrayList<Photo>();
    }


    /**
     * Indicates if the item at position position is selected
     *
     * @param photo Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override
    public boolean isSelected(Photo photo) {
        return getSelectedPhotos().contains(photo);
    }


    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photo Photo of the item to toggle the selection status for
     */
    @Override
    public void toggleSelection(Photo photo) {
        if (selectedPhotos.contains(photo)) {
            selectedPhotos.remove(photo);
        } else {
            selectedPhotos.add(photo);
        }
    }


    /**
     * Clear the selection status for all items
     */
    @Override
    public void clearSelection() {
        selectedPhotos.clear();
    }


    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }


    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }


    public List<Photo> getCurrentPhotos() {
        if (currentDirectoryIndex < 0 || LangUtils.isEmpty(photoDirectories) || currentDirectoryIndex >= photoDirectories.size()) {
            return new ArrayList<Photo>();
        }

        return photoDirectories.get(currentDirectoryIndex).getPhotos();
    }


    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<String>(/*getCurrentPhotos().size()*/);
        for (Photo photo : getCurrentPhotos()) {
            currentPhotoPaths.add(photo.getPath());
        }
        return currentPhotoPaths;
    }


    @Override
    public List<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }


    @Override
    public void setSelectedPhotos(List<Photo> selectedPhotos) {
        if (LangUtils.isEmpty(selectedPhotos))
            return;
        this.selectedPhotos.clear();
        this.selectedPhotos.addAll(selectedPhotos);
    }
}

