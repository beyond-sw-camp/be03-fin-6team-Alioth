<template>
    <div ref="quillEditor" class="editor"></div>
</template>

<script>
import Quill from 'quill';
import 'quill/dist/quill.snow.css';
import { ref, onMounted, watch } from 'vue';

import '@fortawesome/fontawesome-free/css/all.css';
import ImageResize from 'quill-image-resize';

Quill.register('modules/imageResize', ImageResize);
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
    const baseUrl = import.meta.env.VITE_API_SERVER_BASE_URL || 'http://localhost:8080';

    onMounted(() => {
        const icons = Quill.import('ui/icons');
        icons['image'] = '<i class="fas fa-image"></i>'; // 'image' 아이콘을 FontAwesome 아이콘으로 변경

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
            ],
            imageResize: {}
        }
    });



    console.log("Quill editor initialized");

    const Clipboard = Quill.import('modules/clipboard');

    class CustomClipboard extends Clipboard {

            onPaste(e) {
        if (!isUploading) { // isUploading 플래그가 false인 경우에만 이미지 처리
            super.onPaste(e);
            const clipboardData = e.clipboardData || window.clipboardData;
            const items = clipboardData.items;
            for (let i = 0; i < items.length; i++) {
                if (items[i].type.indexOf("image") === 0) {
                    const file = items[i].getAsFile();
                    uploadImageToServer(file).then(imageUrl => {
                        const range = quill.getSelection(true);
                        quill.insertEmbed(range.index, 'image', imageUrl);
                        quill.setSelection(range.index + 1);
                    }).catch(error => {
                        console.error('Failed to upload image:', error);
                    });
                    e.preventDefault();
                }
            }
        }
    }
}
    Quill.register('modules/clipboard', CustomClipboard, true);



    quill.getModule('toolbar').addHandler('image', () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();

    input.onchange = () => {
        const file = input.files[0];
        if (file) {
        uploadImageToServer(file).then(imageUrl => {
            const range = quill.getSelection(true);
            quill.insertEmbed(range.index, 'image', imageUrl);
            quill.setSelection(range.index + 1);
        }).catch(error => {
            console.error('Failed to upload image:', error);
        });
        }
    };
});

    if (props.initialContent) {
    quill.root.innerHTML = props.initialContent;
    }
    quill.on('text-change', () => {
    const html = quill.root.innerHTML;
    emit('update:content', html);
    });
    let isUploading = false; // 업로드 중인지 추적하는 변수

//     function removeDuplicateImages() {
//     const images = quill.root.getElementsByTagName('img');
//     const seenUrls = new Set();

//     Array.from(images).forEach((img) => {
//         if (seenUrls.has(img.src)) {
//         // Remove the duplicate image node
//         img.parentNode.removeChild(img);
//         } else {
//         seenUrls.add(img.src);
//         }
//     });
// }

        function removeExtraImageAfterDrop() {
        const images = quill.root.getElementsByTagName('img');
        if (images.length > 1) {
            const lastImage = images[images.length - 1]; // Get the last image element
            if (lastImage) {
            lastImage.parentNode.removeChild(lastImage); // Remove the last image
            console.log('Extra image removed');
            }
        }
    }


    // const dropContainer = quill.root;
        quill.root.addEventListener('drop', (e) => {
            e.preventDefault();
            if (isUploading) {
                console.log('Upload already in progress.');
                return;
            }

    // quill.root.addEventListener('drop', (e) => {
    // e.preventDefault();
    // // isUploading = true;  // 기본 드롭 이벤트 방지
    // if (isUploading) {
    //     console.log('Upload already in progress.');
    //     return false;
    // }

const files = e.dataTransfer.files;
    if (files.length > 0 && files[0].type.startsWith('image/')) {
        isUploading = true;
        const file = files[0];
        console.log(`Uploading ${file.name}...`);

        uploadImageToServer(files[0])
            .then(imageUrl => {
                const range = quill.getSelection(true) || { index: quill.getLength() };
                quill.insertEmbed(range.index, 'image', imageUrl, 'user');
                console.log(`Image inserted at index ${range.index}.`);
                // removeDuplicateImages();
                removeExtraImageAfterDrop();
            }).catch(error => {
                console.error('Image upload failed:', error);
            }).finally(() => {
                isUploading = false;
            });
        } else {
            console.log('No image files to process.');
        }
    });

    quill.on('text-change', () => {
        const html = quill.root.innerHTML;
        emit('update:content', html);
    });

        if (props.initialContent) {
            quill.root.innerHTML = props.initialContent;
        }
    });


function uploadImageToServer(file) {
    const formData = new FormData();
    formData.append('image', file);

    return fetch(`${baseUrl}/api/image/upload`, {
        method: 'POST',
        body: formData,
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Server response wasn\'t OK');
        }
        return response.json();
    })
    .then(data => {
        return data.imageUrl;
    });
}

watch(() => props.initialContent, (newVal) => {
    if (quill) {
        const currentHtml = quill.root.innerHTML;
        if(newVal !== currentHtml && !(newVal === '<p><br></p>' && !currentHtml.trim())) {
            quill.root.innerHTML = newVal;
        }
    }
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
