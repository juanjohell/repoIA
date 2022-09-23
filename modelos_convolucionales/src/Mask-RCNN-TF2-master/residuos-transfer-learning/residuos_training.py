import os
import xml.etree
from numpy import zeros, asarray

import mrcnn.utils
import mrcnn.config
import mrcnn.model
#numero de imagenes que conformaran el dataset de entrenamiento (80%)
NUM_IMAGES_TRAIN = 1096
class ResiduosDataset(mrcnn.utils.Dataset):

    def load_dataset(self, dataset_dir, is_train=True):
        # Adds information (image ID, image path, and annotation file path) about each image in a dictionary.
        self.add_class("dataset", 1, "botella")
        images_dir = dataset_dir + '/images/'
        annotations_dir = dataset_dir + '/annots/'
        num_ficheros = 0
        for filename in os.listdir(images_dir):
            num_ficheros = num_ficheros + 1
            imagen_nombre = filename.split(".")[0]


            if is_train and int(num_ficheros) >= NUM_IMAGES_TRAIN :  #el 80% de imagenes del dataset
                continue

            if not is_train and int(num_ficheros) < NUM_IMAGES_TRAIN :
                continue

            img_path = images_dir + filename
            ann_path = annotations_dir + imagen_nombre + '.xml'

            self.add_image('dataset', image_id=imagen_nombre, path=img_path, annotation=ann_path)
            
            
            
    # Loads the binary masks for an image.
    def load_mask(self, image_id):
        info = self.image_info[image_id]
        path = info['annotation']
        boxes, w, h = self.extract_boxes(path)
        masks = zeros([h, w, len(boxes)], dtype='uint8')

        class_ids = list()
        for i in range(len(boxes)):
            box = boxes[i]
            row_s, row_e = box[1], box[3]
            col_s, col_e = box[0], box[2]
            masks[row_s:row_e, col_s:col_e, i] = 1
            class_ids.append(self.class_names.index('botella'))
        return masks, asarray(class_ids, dtype='int32')

    # A helper method to extract the bounding boxes from the annotation file
    def extract_boxes(self, filename):
        tree = xml.etree.ElementTree.parse(filename)

        root = tree.getroot()

        boxes = list()
        for box in root.findall('.//bndbox'):
            xmin = int(box.find('xmin').text)
            ymin = int(box.find('ymin').text)
            xmax = int(box.find('xmax').text)
            ymax = int(box.find('ymax').text)
            coors = [xmin, ymin, xmax, ymax]
            boxes.append(coors)

        width = int(root.find('.//size/width').text)
        height = int(root.find('.//size/height').text)
        return boxes, width, height

class ResiduosConfig(mrcnn.config.Config):
    NAME = "residuos_cfg"
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1
    
    NUM_CLASSES = 2
    #ATENCION STEPS_PER_EPOCH DEBE COINCIDIR CON LA DIVISION QUE SE HACE ENTRE TRAIN Y VAL
    #MAS ARRIBA
    STEPS_PER_EPOCH = NUM_IMAGES_TRAIN -1

# Train
train_dataset = ResiduosDataset()
train_dataset.load_dataset(dataset_dir='residuos', is_train=True)
train_dataset.prepare()

# Validation
validation_dataset = ResiduosDataset()
validation_dataset.load_dataset(dataset_dir='residuos', is_train=False)
validation_dataset.prepare()

# Model Configuration
residuos_config = ResiduosConfig()

# Build the Mask R-CNN Model Architecture
model = mrcnn.model.MaskRCNN(mode='training', 
                             model_dir='./', 
                             config=residuos_config)

model.load_weights(filepath='mask_rcnn_coco.h5', 
                   by_name=True, 
                   exclude=["mrcnn_class_logits", "mrcnn_bbox_fc",  "mrcnn_bbox", "mrcnn_mask"])

model.train(train_dataset=train_dataset, 
            val_dataset=validation_dataset, 
            learning_rate=residuos_config.LEARNING_RATE, 
            epochs=5, 
            layers='heads')

model_w_path = 'Residuos_weights_mask_rcnn_trained.h5'
model_path = 'Residuos_mask_rcnn_trained.h5'
model.keras_model.save_weights(model_w_path)
#model.keras_model.save(model_path)
