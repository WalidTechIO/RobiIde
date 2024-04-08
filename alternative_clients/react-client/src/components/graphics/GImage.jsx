import React from 'react'
import { useState } from 'react';

export const GImage = ({image, images}) => {

  const [img, setImg] = useState(<img alt="Loading..." />)
  const [hasLoadedImage, setHasLoadedImage] = useState(false)

  File.prototype.convertToBase64 = function (callback) {
    var reader = new FileReader();
    reader.onloadend = function (e) {
      callback(e.target.result, e.target.error);
    };
    reader.readAsDataURL(this);
  }

  let imgindex = -1

  if(images) {
    for(let i=0; i<images.length;i++) {
      if(images[i].name === image.path) {
        imgindex = i
      }
    }
  }

  if(!hasLoadedImage && imgindex != -1) {
    images[imgindex].convertToBase64((base64) => {
      setImg(<img src={base64} />)
      setHasLoadedImage(true)
    })
  }
  

  const style = {
    position: "absolute",
    top: image.position.height,
    left: image.position.width
  }

  return <div style={style}>
    {img}
  </div>
}
