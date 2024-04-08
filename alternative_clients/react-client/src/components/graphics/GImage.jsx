import React, { useState, useEffect } from 'react';

export const GImage = ({image, images}) => {

  const [img, setImg] = useState(<img alt="Loading..." />)

  File.prototype.convertToBase64 = function (callback) {
    var reader = new FileReader();
    reader.onloadend = function (e) {
      callback(e.target.result, e.target.error);
    };
    reader.readAsDataURL(this);
  }

  

  useEffect(() => {
    let imgindex = -1

    if (images) {
      for (let i = 0; i < images.length; i++) {
        if (images[i].name === image.path) {
          imgindex = i
        }
      }
    }

    if (imgindex != -1) {
      images[imgindex].convertToBase64((base64) => {
        setImg(<img src={base64} />)
      })
    } else {
      setImg(<img alt="Image not found in images list" />)
    }
  }, [])

  const style = {
    position: "absolute",
    top: image.position.height,
    left: image.position.width
  }

  return <div style={style}>
    {img}
  </div>
}
