import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "habits")
public class Habit implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String category;
    private String difficulty; // "Easy", "Medium", "Hard"
    private int iconRes;
    private String color; // Hex color string
    private boolean isCompleted;
    private boolean isArchived;
    private long completedDate;

    public Habit(String id, String title, String category, String difficulty, int iconRes, String color) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.iconRes = iconRes;
        this.color = color;
        this.isCompleted = false;
        this.isArchived = false;
        this.completedDate = 0;
    }

    public Habit(String title, String category, String difficulty, String color, int iconRes) {
        this(UUID.randomUUID().toString(), title, category, difficulty, iconRes, color);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public int getIconRes() { return iconRes; }
    public String getColor() { return color; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { 
        isCompleted = completed;
        if (completed) {
            this.completedDate = System.currentTimeMillis();
        }
    }
    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { isArchived = archived; }
    public long getCompletedDate() { return completedDate; }
    public void setCompletedDate(long completedDate) { this.completedDate = completedDate; }
}