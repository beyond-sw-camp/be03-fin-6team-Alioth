<template>
    <div ref="quillEditor" class="editor"></div>
</template>

<script>
import Quill from 'quill';
import 'quill/dist/quill.snow.css';

export default {
    name: "QuillEditor",
    emits: ['update:content'],
    props: {
    initialContent: {
        type: String,
        default: ''
        }
    },
    setup(props, { emit }) {
    const quillEditor = ref(null);
    let quill;

    onMounted(() => {
        quill = new Quill(quillEditor.value, {
        theme: 'snow',
        modules: {
            toolbar: [
            ['bold', 'italic', 'underline', 'strike'],
            ['blockquote', 'code-block'],
            [{ header: 1 }, { header: 2 }],
            [{ list: 'ordered'}, { list: 'bullet' }],
            [{ script: 'sub'}, { script: 'super' }],
            [{ indent: '-1'}, { indent: '+1' }],
            [{ direction: 'rtl' }],
            [{ size: ['small', false, 'large', 'huge'] }],
            [{ header: [1, 2, 3, 4, 5, 6, false] }],
            [{ color: [] }, { background: [] }],
            [{ font: [] }],
            [{ align: [] }],
            ['clean'],
            ['link', 'image', 'video']
        ]
        }
    });
        if (props.initialContent) {
        quill.root.innerHTML = props.initialContent;
        }
        quill.on('text-change', () => {
        const html = quill.root.innerHTML;
        emit('update:content', html);
        });
    });
        return {
            quillEditor
        };
    }
};
</script>

<style scoped>
.editor {
    height: 400px;
}
</style>
