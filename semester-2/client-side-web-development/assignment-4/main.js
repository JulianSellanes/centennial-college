const tasks = document.querySelectorAll(".task");
const dropzones = document.querySelectorAll(".dropzone");

tasks.forEach(task => {
    task.addEventListener("dragstart", dragStart);
    task.addEventListener("dragend", dragEnd);
});

dropzones.forEach(dropzone => {
    dropzone.addEventListener("dragover", dragOver);
    dropzone.addEventListener("dragenter", dragEnter);
    dropzone.addEventListener("dragleave", dragLeave);
    dropzone.addEventListener("drop", dragDrop);
});

function dragStart(event) {
    event.dataTransfer.setData("text", event.target.id);
    event.target.classList.add("dragging");
}

function dragEnd(event) {
    event.target.classList.remove("dragging");
}

function dragOver(event) {
    event.preventDefault();
}

function dragEnter(event) {
    event.target.classList.add("drop");
}

function dragLeave(event) {
    event.target.classList.remove("drop");
}

function dragDrop(event) {
    event.preventDefault();
    event.target.classList.remove("drop");
    const draggedTask = document.getElementById(event.dataTransfer.getData("text"));

    if(!this.contains(draggedTask))
        this.appendChild(draggedTask);
}