import GRect from "./GRect.jsx";
import GImage from "./GImage.jsx";
import GOval from "./GOval.jsx";
import GText from "./GText.jsx";

export default function ObjectRenderer({object, images}) {

    //World rendering
    if(object.type === "GWorld") {
        
        const style = {
            position: "relative",
            backgroundColor: object.color,
            width: `${object.dimension.width}px`,
            height: `${object.dimension.height}px`,
        }

        return <div style={style}>
            {object.childrens.map(children => {
                return (<ObjectRenderer key={children.uuid} object={children} images={images} />)
            })}
        </div>
    }

    //Common objects
    return <>
        {object.type === "GRect" && <GRect rectangle={object} images={images}/>}
        {object.type === "GImage" && <GImage image={object} images={images} />}
        {object.type === "GOval" && <GOval oval={object} images={images} />}
        {object.type === "GText" && <GText text={object} />}
    </>

}